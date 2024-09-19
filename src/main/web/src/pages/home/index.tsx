import * as api from "@/lib/api";
import { useEffect, useState } from "react";

const email = "test@test.com";
const name = "Test";
const password = "Test@Test123";

export function HomePage() {
  const [state, setState] = useState();
  useEffect(() => {
    const fetchData = async () => {
      try {
        let user; //= await api.signUp({ email, name, password });
        // console.log(user);
        user = await api.signIn({ email, password });
        console.log(user);
      } catch (error) {
        console.error(error);
      }
    };
    fetchData();
  }, []);
  return <div>This is home page.</div>;
}
