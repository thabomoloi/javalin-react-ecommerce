import { RouterProvider } from "react-router-dom";
import { router } from "./routes";
import { useAuth } from "./lib/auth";
import { useEffect } from "react";

export default function App() {
  const { checkAuthentication } = useAuth();
  useEffect(() => {
    checkAuthentication();
  }, [checkAuthentication]);
  return <RouterProvider router={router} />;
}
