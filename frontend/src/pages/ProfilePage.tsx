import UserProfile from "../components/ProfileComponent.tsx";
import {useParams} from "react-router-dom";

export default function ProfilePage() {

    const {username} = useParams<{ username: string }>()

    return (
        <div>
            <UserProfile username={username}/>
        </div>
    );
}
