import axios, { AxiosError } from "axios";
import { create } from "zustand";

enum Role {
  NONE = "NONE",
  USER = "USER",
  ADMIN = "ADMIN",
}

interface User {
  id: number; // Changed 'int' to 'id' for clarity
  name: string;
  email: string;
  role: Role;
  emailVerified: string;
}

interface AuthState {
  userIsAuthenticated: boolean;
  currentUser: User | null;
  fetchCurrentUser: () => Promise<void>;
  checkAuthentication: () => Promise<void>;
}

const USER_URL = "/api/users/me";
const REFRESH_URL = "/api/auth/refresh";

const useAuth = create<AuthState>((set) => ({
  userIsAuthenticated: false,
  currentUser: null,

  // Function to fetch the current user
  fetchCurrentUser: async () => {
    const response = await axios.get(USER_URL);
    if (response.status === 200) {
      set({ currentUser: response.data, userIsAuthenticated: true });
    }
  },

  checkAuthentication: async () => {
    try {
      await useAuth.getState().fetchCurrentUser(); // Try fetching the user
    } catch (error) {
      if (error instanceof AxiosError) {
        // Handle 401 Unauthorized - Attempt token refresh
        if (error.response?.status === 401) {
          try {
            await axios.post(REFRESH_URL); // Try refreshing the token
            await useAuth.getState().fetchCurrentUser(); // Retry fetching the user
          } catch (refreshError) {
            console.error("Token refresh failed", refreshError);
            set({ currentUser: null, userIsAuthenticated: false });
          }
        } else {
          console.error("Authentication error", error.response?.status);
        }
      } else {
        console.error("Unexpected error", error);
      }
    }
  },
}));

export { useAuth };
