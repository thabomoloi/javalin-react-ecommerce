import { CircleUserRound, Menu, ShoppingCart } from "lucide-react";
import { Logo } from "../logo";
import { Button } from "../ui/button";
import { SearchForm } from "./search-form";
import { AccountSidebar, SidebarTrigger } from "../sidebar";
import { NavLink } from "react-router-dom";

export function ShoppingCardButton() {
  return (
    <Button size="icon" variant="ghost">
      <ShoppingCart />
    </Button>
  );
}

export function AccountButton() {
  return (
    <AccountSidebar>
      <SidebarTrigger>
        <CircleUserRound />
      </SidebarTrigger>
    </AccountSidebar>
  );
}

export function HeaderMobile() {
  return (
    <div>
      <div className="flex items-center justify-between gap-4 px-4 py-2">
        <Button size="icon" variant="ghost">
          <Menu />
        </Button>
        <Logo className="w-40" />
        <div className="flex gap-2 items-center">
          <ShoppingCardButton />
          <AccountButton />
        </div>
      </div>
      <div className="flex justify-center bg-primary1 p-4">
        <SearchForm />
      </div>
    </div>
  );
}

export function Header() {
  return (
    <header>
      <HeaderMobile />
    </header>
  );
}

export function AuthHeader() {
  return (
    <header className="border-b bg-background">
      <div className="flex items-center justify-center p-4">
        <NavLink to="/">
          <Logo className="w-40" />
        </NavLink>
      </div>
    </header>
  );
}
