import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

type LandingPageProps = {
  username: string | undefined;
};

export default function LandingPage(userName: LandingPageProps) {
  const navigate = useNavigate();

  function login() {
    const protocol = window.location.protocol;
    const host =
      window.location.host === "localhost:5173"
        ? `${protocol}//localhost:8080`
        : `${protocol}//${window.location.host}`;
    window.open(host + "/oauth2/authorization/github", "_self");
  }

  useEffect(() => {
    async function checkUserName() {
      if (userName.username) {
        await navigate("/feed");
      }
    }
    void checkUserName();
  }, [userName, navigate]);

  return (
    <>
      <button onClick={login}>Login</button>
    </>
  );
}
