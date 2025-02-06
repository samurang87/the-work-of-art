import { Navigate, Outlet } from "react-router-dom";

type ProtectedRouteProps = {
  username: string | undefined;
};

export default function ProtectedRoutes(props: Readonly<ProtectedRouteProps>) {
  const isLoggedIn: boolean =
    props.username !== "" &&
    props.username !== "anonymousUser" &&
    props.username !== undefined;

  return <>{isLoggedIn ? <Outlet /> : <Navigate to={"/"} />}</>;
}
