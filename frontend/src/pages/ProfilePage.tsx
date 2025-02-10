import UserProfile from "../components/ProfileComponent.tsx";
import { useParams } from "react-router-dom";

type ProfilePageProps = {
  loggedInUsername: string | undefined;
};

export default function ProfilePage({ loggedInUsername }: ProfilePageProps) {
  const { username } = useParams<{ username: string }>();

  if (!username) {
    return <div>User not found</div>;
  }

  return (
    <div className="container mx-auto px-4 mt-24 pb-24">
      <UserProfile username={username} loggedInUsername={loggedInUsername} />
    </div>
  );
}
