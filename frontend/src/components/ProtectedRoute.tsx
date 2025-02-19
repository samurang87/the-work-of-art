import { Navigate, Outlet } from "react-router-dom";

type ProtectedRouteProps = {
  username: string;
  isLoading: boolean;
};

export default function ProtectedRoutes({
  username,
  isLoading,
}: Readonly<ProtectedRouteProps>) {
  if (isLoading) {
    return null; // TODO: loading spinner?
  }

  if (!username) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
}
