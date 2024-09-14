import { zodResolver } from "@hookform/resolvers/zod";
import { SignInSchema, SignInSchemaType } from "@/lib/zod";
import { useForm } from "react-hook-form";
import { Link, useSubmit } from "react-router-dom";
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

export function SignInPage() {
  const submit = useSubmit();
  const form = useForm<SignInSchemaType>({
    resolver: zodResolver(SignInSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const onSubmit = (data: SignInSchemaType) => {
    submit(data, {
      method: "post",
      action: "/auth/signin",
      encType: "application/json",
    });
  };

  return (
    <Card className="w-full max-w-md">
      <CardHeader>
        <CardTitle className="text-xl text-center">Welcome Back!</CardTitle>
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)}>
            <div className="space-y-3">
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
                          autoComplete="current-password"
                        />
                      </PasswordField>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
            <Button type="submit" className="w-full mt-6">
              Sign in
            </Button>
          </form>
        </Form>
      </CardContent>
      <CardFooter className="flex-col text-sm text-center">
        <p>
          <Link to="#/auth/forgot-password" className="font-bold text-primary1">
            Forgot Password?
          </Link>
        </p>
        <p>
          Don't have an account?{" "}
          <Link to="/auth/signup" className="font-bold text-primary1">
            Sign up
          </Link>
          .
        </p>
      </CardFooter>
    </Card>
  );
}
