import { zodResolver } from "@hookform/resolvers/zod";
import { SignUpSchema, SignUpSchemaType } from "@/lib/zod";
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

export function SignUpPage() {
  const submit = useSubmit();
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

  return (
    <Card className="w-full max-w-md">
      <CardHeader>
        <CardTitle className="text-xl text-center">Join Us!</CardTitle>
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
