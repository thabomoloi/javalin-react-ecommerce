import { Search } from "lucide-react";
import { Button } from "../ui/button";
import { Input } from "../ui/input";

export function SearchForm() {
  return (
    <div className="w-full">
      <form>
        <div className="flex items-center">
          <Input
            placeholder="Search products, categories ...."
            name="query"
            className="rounded-full px-5 pr-12 bg-neutral-50"
            required
          />
          <Button
            variant="ghost"
            className="-ml-12 w-12 h-9 p-0 rounded-full  hover:bg-transparent"
          >
            <Search />
          </Button>
        </div>
      </form>
    </div>
  );
}
