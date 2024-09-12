import { CircleUserRound, Menu, ShoppingCart } from "lucide-react";
import { Logo } from "../logo";
import { Button } from "../ui/button";
import { SearchForm } from "./search-form";

export function HeaderMobile() {
  return (
    <div>
      <div className="flex items-center justify-between gap-4 px-4 py-2">
        <Button size="icon" variant="ghost">
          <Menu />
        </Button>
        <Logo className="w-40" />
        <div>
          <Button size="icon" variant="ghost">
            <ShoppingCart />
          </Button>
          <Button size="icon" variant="ghost" className="ml-2">
            <CircleUserRound />
          </Button>
        </div>
      </div>
      <div className="flex justify-center bg-primaryGreen-7 p-4">
        <SearchForm />
      </div>
    </div>
  );
}
