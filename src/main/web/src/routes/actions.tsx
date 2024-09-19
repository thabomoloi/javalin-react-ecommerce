import * as api from "@/lib/api";
import { SignInSchemaType, SignUpSchemaType } from "@/lib/zod";
import { redirect, ActionFunctionArgs } from "react-router-dom";

// interface Updates {
//   [key: string]: FormDataEntryValue; // This covers form data entries which could be strings or File objects
// }

export async function signUpAction({ request }: ActionFunctionArgs) {
  const data = (await request.json()) as SignUpSchemaType;
  await api.signUp(data);
  return redirect("/auth/signin");
}

export async function signInAction({ request }: ActionFunctionArgs) {
  const data = (await request.json()) as SignInSchemaType;
  await api.signIn(data);
  return redirect("/");
}
