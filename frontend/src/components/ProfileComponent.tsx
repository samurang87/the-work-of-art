import {useEffect, useState} from "react";
import axios from "axios";
import {User} from "../types.tsx";

type UserProfileProps = {
    username?: string;
};

export default function UserProfile({username}: UserProfileProps) {
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        async function fetchUser(username: string | undefined) {
            if (!username) {
                setError('Username is required');
                setLoading(false);
                return;
            }
setLoading(true);  // Reset loading when username changes
            try {
                const response = await axios.get<User>('/api/user/' + username);
                setUser(response.data);
                setError(null); // ✅ Clear previous errors on success
            } catch (error) {
                setError(error instanceof Error ? error.message : 'An error occurred');
            } finally {
                setLoading(false);
            }
        }

        void fetchUser(username);
    }, [username]);

    if (loading) return <div className="mt-24 text-center">Loading...</div>;
    if (error) return <div
        className="mt-24 text-center text-red-500">Error: {error}</div>;
    if (!user) return <div className="mt-24 text-center">No user data available</div>;

    return (
        <div className="container mx-auto px-4 mt-24">
            <div className="max-w-2xl mx-auto space-y-6">
                {/* Profile Section */}
                <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
                    <h2 className="text-2xl font-semibold text-gray-800">{user.name}</h2>
                </div>

                {/* Bio Section */}
                <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
                    <h3 className="text-xl font-semibold text-gray-800 mb-4">Bio</h3>
                    <p className="text-gray-600">
                        {user.bio || 'No bio yet 🌳'}
                    </p>
                </div>

                {/* Mediums Section */}
                <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
                    <h3 className="text-xl font-semibold text-gray-800 mb-4">Mediums</h3>
                    {user.mediums && user.mediums.length > 0 ? (
                        <ul className="space-y-2">
                            {user.mediums.map((medium) => (
                                <li
                                    key={medium}
                                    className="text-gray-600 py-2 border-b border-gray-100 last:border-0"
                                >
                                    {medium}
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p className="text-gray-600">No mediums yet 🖌️</p>
                    )}
                </div>
            </div>
        </div>
    );
}
