import { zodResolver } from "@hookform/resolvers/zod";
import { SignUpSchema, SignUpSchemaType } from "@/lib/zod";
import { useForm } from "react-hook-form";
import { Link, useActionData, useSubmit } from "react-router-dom";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { PasswordField } from "@/components/ui/password-field";
import { ErrorDTO, ValidationErrorDTO } from "@/lib/dtos";
import { useEffect } from "react";

export function SignUpPage() {
  const submit = useSubmit();
  const error = useActionData() as ErrorDTO;

  const form = useForm<SignUpSchemaType>({
    resolver: zodResolver(SignUpSchema),
    defaultValues: {
      email: "",
      name: "",
      password: "",
    },
  });

  const onSubmit = (data: SignUpSchemaType) => {
    submit(data, {
      method: "post",
      action: "/auth/signup",
      encType: "application/json",
    });
  };

  useEffect(() => {
    const findError = (key: string) => {
      const { errors } = error as ValidationErrorDTO;
      return errors.find((err) => err.property == key);
    };
    if (error) {
      if (error.errors) {
        const keys: ("email" | "name" | "password")[] = [
          "email",
          "name",
          "password",
        ];
        for (const key of keys) {
          const err = findError(key);
          if (err) {
            form.setError(key, {
              type: "manual",
              message: err.message,
            });
          }
        }
      }
    }
  }, [error, form]);

  return (
    <Card className="w-full max-w-md">
      <CardHeader className="gap-4">
        <CardTitle className="text-xl text-center">Join Us!</CardTitle>
        {error && !error.errors && (
          <div className="text-sm text-center font-semibold rounded border border-destructive bg-red-1 text-red-5 p-4">
            <p>{error.message}</p>
          </div>
        )}
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)}>
            <div className="space-y-3">
              <FormField
                control={form.control}
                name="name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Name</FormLabel>
                    <FormControl>
                      <Input
                        required
                        {...field}
                        type="text"
                        className="bg-neutral-1"
                        autoComplete="name"
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Email</FormLabel>
                    <FormControl>
                      <Input
                        required
                        {...field}
                        type="email"
                        className="bg-neutral-1"
                        autoComplete="email"
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Password</FormLabel>
                    <FormControl>
                      <PasswordField>
                        <Input
                          required
                          {...field}
                          type="password"
                          className="bg-neutral-1"
                        />
                      </PasswordField>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <Button type="submit" className="w-full mt-6">
              Sign up
            </Button>
          </form>
        </Form>
      </CardContent>
      <CardFooter className="flex-col gap-3 text-sm text-center">
        <p className="text-center text-xs text-neutral-6">
          By signing up, you agree to our{" "}
          <Link to="/terms" className="font-semibold text-primary1">
            Terms of Service
          </Link>
          {" and "}
          <Link to="/privacy" className="font-semibold text-primary1">
            Privacy Policy
          </Link>
        </p>
        <p>
          Already have an account?{" "}
          <Link to="/auth/signin" className="font-bold text-primary1">
            Sign in
          </Link>
          .
        </p>
      </CardFooter>
    </Card>
  );
}
