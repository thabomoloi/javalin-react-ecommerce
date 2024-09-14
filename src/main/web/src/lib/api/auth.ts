import { SignUpSchemaType } from "../zod";
import axios from "axios";

async function signUp(data: SignUpSchemaType) {
  // try {
  const response = await axios.post("/api/users", data);
  return response.data;
  // } catch (error) {
  //   console.log(error);
  // }
}

export { signUp };
