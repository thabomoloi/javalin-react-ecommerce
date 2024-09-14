import { AuthLayout } from "@/pages/auth/auth-layout";
import { SignUpPage } from "@/pages/auth/signup";
import { SignInPage } from "@/pages/auth/signin";
import { HomePage } from "@/pages/home";
import { RootLayout } from "@/pages/root-layout";
import { createBrowserRouter } from "react-router-dom";
import { signUpAction } from "./actions";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <RootLayout />,
    children: [{ index: true, element: <HomePage /> }],
  },
  {
    path: "/auth",
    element: <AuthLayout />,
    children: [
      { path: "signin", element: <SignInPage /> },
      { path: "signup", action: signUpAction, element: <SignUpPage /> },
    ],
  },
]);
