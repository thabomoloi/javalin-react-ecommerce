import * as api from "@/lib/api";
import { ErrorDTO, ValidationErrorDTO } from "@/lib/dtos";
import { SignUpSchemaType } from "@/lib/zod";
import { AxiosError } from "axios";
import { redirect, ActionFunctionArgs } from "react-router-dom";

// interface Updates {
//   [key: string]: FormDataEntryValue; // This covers form data entries which could be strings or File objects
// }

export async function signUpAction({ request }: ActionFunctionArgs) {
  const data = (await request.json()) as SignUpSchemaType;
  try {
    await api.signUp(data);
  } catch (error) {
    if (error instanceof AxiosError) {
      const data = error.response?.data as ErrorDTO;
      if (data.code == 400 && data.errors) {
        return data as ValidationErrorDTO;
      }
      if (data.code == 400) {
        return data;
      }
    }
    throw error;
  }

  return redirect(`/auth/signin`);
}
