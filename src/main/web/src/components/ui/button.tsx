import * as React from "react";
import { Slot } from "@radix-ui/react-slot";
import { cva, type VariantProps } from "class-variance-authority";

import { cn } from "@/lib/utils";

const buttonVariants = cva(
  "inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-primary2-10 disabled:pointer-events-none disabled:opacity-50 dark:focus-visible:ring-primary2-3",
  {
    variants: {
      variant: {
        default: "bg-primary2 text-primary2-foreground hover:bg-primary2/90",
        destructive:
          "bg-destructive text-destructive-foreground shadow-sm hover:bg-destructive/90",
        warning:
          "bg-warning text-warning-foreground shadow-sm hover:bg-warning/90",
        outline:
          "border border-neutral-2 bg-white shadow-sm hover:bg-neutral-1 hover:text-neutral-9",
        secondary:
          "bg-neutral-1 text-neutral-9 shadow-sm hover:bg-neutral-1/80",
        ghost: "hover:bg-neutral-1 hover:text-neutral-9",
        link: "text-neutral-9 underline-offset-4 hover:underline",
      },
      size: {
        default: "h-9 px-4 py-2",
        sm: "h-8 rounded-md px-3 text-xs",
        lg: "h-10 rounded-md px-8",
        icon: "h-9 w-9",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  }
);

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean;
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, asChild = false, ...props }, ref) => {
    const Comp = asChild ? Slot : "button";
    return (
      <Comp
        className={cn(buttonVariants({ variant, size, className }))}
        ref={ref}
        {...props}
      />
    );
  }
);
Button.displayName = "Button";

export { Button, buttonVariants };
