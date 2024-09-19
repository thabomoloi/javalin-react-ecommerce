import { SignInSchemaType, SignUpSchemaType } from "../zod";
import axios from "axios";

async function signUp(data: SignUpSchemaType) {
  const response = await axios.post("/api/auth/signup", data);
  return response.data;
}
async function signIn(data: SignInSchemaType) {
  const response = await axios.post("/api/auth/signin", data);
  return response.data;
}
async function fetchCurrentUser() {
  const response = await axios.get("/api/users/me");
  return response.data;
}
export { signUp, signIn, fetchCurrentUser };
