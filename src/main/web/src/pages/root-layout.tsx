import { Header } from "@/components/header";
import { Outlet } from "react-router-dom";

export function RootLayout() {
  return (
    <div>
      <Header />
      <main>
        <Outlet />
      </main>
    </div>
  );
}
