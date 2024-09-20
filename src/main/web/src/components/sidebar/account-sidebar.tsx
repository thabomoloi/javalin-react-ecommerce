import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "../ui/sheet";
import { Logo } from "../logo";
import { Separator } from "../ui/separator";
import { Button } from "../ui/button";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "@/lib/auth";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import { LogOut, Package, Settings, User } from "lucide-react";

// Sidebar for users who are not authenticated
function GuestAccountSidebar() {
  const navigate = useNavigate();
  return (
    <SheetContent className="flex flex-col">
      <div className="overflow-auto">
        <SheetHeader>
          <SheetTitle>
            <Logo className="w-40" />
          </SheetTitle>
          <SheetDescription className="font-semibold text-left">
            Welcome to Oasis Nourish.
          </SheetDescription>
        </SheetHeader>
        <Separator className="mt-4 mb-6" />
        <div>
          <div>
            <p className="font-bold mb-2">I have an account.</p>
            <p className="mb-6 text-neutral-10 text-sm">
              Fill in your email and password to access your account.
            </p>
            <Button
              className="w-full"
              onClick={() => navigate("/auth/signin")}
              variant="secondary"
            >
              Sign in
            </Button>
          </div>
          <Separator className="my-4" />
          <div>
            <p className="font-bold mb-2">I am a new customer.</p>
            <p className="mb-2 text-neutral-10 text-sm">
              By creating an account with Oasis Nourish, you will be able to
            </p>
            <ul className="ml-6 mb-6 space-y-1 text-neutral-8 list-disc list-inside text-sm">
              <li>Checkout faster</li>
              <li>View and track your order</li>
              <li>Add multiple addresses</li>
              <li>Earn rewards</li>
            </ul>
            <Button onClick={() => navigate("/auth/signup")} className="w-full">
              Sign up
            </Button>
          </div>
        </div>
      </div>
    </SheetContent>
  );
}

// Sidebar for authenticated users
function UserAccountSidebar() {
  return (
    <DropdownMenuContent className="w-48">
      <DropdownMenuLabel className="font-bold">My Account</DropdownMenuLabel>
      <DropdownMenuGroup className="font-semibold">
        <DropdownMenuItem>
          <Link to="/account/profile" className="flex items-center gap-4">
            <User /> Profile
          </Link>
        </DropdownMenuItem>
        <DropdownMenuItem>
          <Link to="/account/orders" className="flex items-center gap-4">
            <Package /> Orders
          </Link>
        </DropdownMenuItem>
        <DropdownMenuItem>
          <Link to="/account/settings" className="flex items-center gap-4">
            <Settings /> Settings
          </Link>
        </DropdownMenuItem>
      </DropdownMenuGroup>
      <DropdownMenuSeparator />
      <DropdownMenuItem className="font-semibold">
        <Link to="/auth/signout" className="flex items-center gap-4">
          <LogOut /> Sign out
        </Link>
      </DropdownMenuItem>
    </DropdownMenuContent>
  );
}

export function AccountSidebar({ children }: { children: React.ReactNode }) {
  const { userIsAuthenticated } = useAuth();
  if (!userIsAuthenticated) {
    return (
      <Sheet>
        {children}
        <GuestAccountSidebar />
      </Sheet>
    );
  }
  return (
    <DropdownMenu>
      {children}
      <UserAccountSidebar />
    </DropdownMenu>
  );
}

export function AccountTrigger({ children }: { children: React.ReactNode }) {
  const { userIsAuthenticated } = useAuth();
  const Trigger = userIsAuthenticated ? DropdownMenuTrigger : SheetTrigger;
  return <Trigger>{children}</Trigger>;
}
