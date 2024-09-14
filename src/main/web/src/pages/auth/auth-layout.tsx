import { Footer } from "@/components/footer";
import { AuthHeader } from "@/components/header";
import { Outlet } from "react-router-dom";

export function AuthLayout() {
  return (
    <div className="h-full flex flex-col">
      <AuthHeader />
      <main className="p-4 flex-grow">
        <div className="flex flex-col justify-center items-center h-full">
          <Outlet />
        </div>
      </main>
      <Footer />
    </div>
  );
}
