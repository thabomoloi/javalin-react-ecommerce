import { useAuth } from "@/lib/auth";

export function HomePage() {
  const auth = useAuth();
  return (
    <div>
      {auth.currentUser == null ? "null" : JSON.stringify(auth.currentUser)}
    </div>
  );
}
