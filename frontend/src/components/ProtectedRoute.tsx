import { Navigate, Outlet } from "react-router-dom";

type ProtectedRouteProps = {
  username: string | undefined;
};

export default function ProtectedRoutes({
  username,
}: Readonly<ProtectedRouteProps>) {
  if (!username) {
    return <Navigate to="/" replace />;
  }
  return <Outlet />;
}
