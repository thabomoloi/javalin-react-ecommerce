import React from "react";
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
} from "../ui/sheet";
import { Logo } from "../logo";
import { Separator } from "../ui/separator";
import { Button } from "../ui/button";

interface AccountSidebarProps {
  children: React.ReactNode;
  authenticated?: boolean;
}

// Sidebar for users who are not authenticated
function GuestAccountSidebar() {
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
            <p className="font-bold mb-2 text-lg">I have an account.</p>
            <p className="mb-6 text-neutralCoolGray-10">
              Fill in your email and password to access your account.
            </p>
            <Button className="w-full" variant="secondary">
              Sign in
            </Button>
          </div>
          <Separator className="my-4" />
          <div>
            <p className="font-bold mb-2 text-lg">I am a new customer.</p>
            <p className="mb-2 text-neutralCoolGray-10">
              By creating an account with Oasis Nourish, you will be able to
            </p>
            <ul className="ml-6 mb-6 space-y-1 text-neutralCoolGray-8 list-disc list-inside">
              <li>Checkout faster</li>
              <li>View and track your order</li>
              <li>Add multiple addresses</li>
              <li>Earn rewards</li>
            </ul>
            <Button className="w-full">Sign up</Button>
          </div>
        </div>
      </div>
    </SheetContent>
  );
}

// Sidebar for authenticated users
function UserAccountSidebar() {
  return (
    <SheetContent className="flex flex-col">
      <div className="overflow-auto">
        <p>Authenticated User Content</p>
      </div>
    </SheetContent>
  );
}

export function AccountSidebar({
  children,
  authenticated = false,
}: AccountSidebarProps) {
  return (
    <Sheet>
      {children}
      {authenticated ? <UserAccountSidebar /> : <GuestAccountSidebar />}
    </Sheet>
  );
}
