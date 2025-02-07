import { useEffect, useState } from "react";
import axios from "axios";
import { User, UserEditRequestPayload, Medium } from "../types.tsx";

type UserProfileProps = {
  username?: string;
};

export default function UserProfile({ username }: UserProfileProps) {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState<User | null>(null);

  useEffect(() => {
    async function fetchUser(username: string | undefined) {
      if (!username) {
        setError("Username is required");
        setLoading(false);
        return;
      }
      setLoading(true); // Reset loading when username changes
      try {
        const response = await axios.get<User>("/api/user/", {
          params: {
            name: username,
          },
        });
        setUser(response.data);
        setFormData(response.data);
        setError(null); // ✅ Clear previous errors on success
      } catch (error) {
        setError(error instanceof Error ? error.message : String(error));
      } finally {
        setLoading(false);
      }
    }

    void fetchUser(username);
  }, [username]);

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleSave = async () => {
    if (!formData) return;

    const payload: UserEditRequestPayload = {
      bio: formData.bio,
      imageUrl: formData.imageUrl,
      mediums: formData.mediums,
    };

    try {
      await axios.put(`/api/user/${user?.id}`, payload);
      setUser(formData);
      setIsEditing(false);
    } catch (error) {
      setError(error instanceof Error ? error.message : String(error));
    }
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => {
    setFormData({ ...formData, [e.target.name]: e.target.value } as User);
  };

  const handleMediumChange = (medium: Medium) => {
    if (!formData) return;
    const updatedMediums = formData.mediums.includes(medium)
      ? formData.mediums.filter((m) => m !== medium)
      : [...formData.mediums, medium];
    setFormData({ ...formData, mediums: updatedMediums });
  };

  if (loading) return <div className="mt-24 text-center">Loading...</div>;
  if (error)
    return <div className="mt-24 text-center text-red-500">Error: {error}</div>;
  if (!user)
    return <div className="mt-24 text-center">No user data available</div>;

  return (
    <div className="container mx-auto px-4 mt-24">
      <div className="max-w-2xl mx-auto space-y-6">
        <button onClick={handleEdit} className="text-blue-500">
          Edit 📝
        </button>
        {/* Profile Section */}
        <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
          <h2 className="text-2xl font-semibold text-gray-800">{user.name}</h2>
        </div>

        {/* Bio Section */}
        <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
          <h3 className="text-xl font-semibold text-gray-800 mb-4">Bio</h3>
          {isEditing ? (
            <textarea
              name="bio"
              value={formData?.bio || ""}
              onChange={handleChange}
              className="w-full bg-transparent border-none focus:outline-none"
            />
          ) : (
            <p className="text-gray-600">{user.bio || "No bio yet 🌳"}</p>
          )}
        </div>

        {/* Mediums Section */}
        <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
          <h3 className="text-xl font-semibold text-gray-800 mb-4">Mediums</h3>
          {isEditing ? (
            <div className="space-y-2">
              {Object.values(Medium).map((medium) => (
                <label key={medium} className="flex items-center space-x-2">
                  <input
                    type="checkbox"
                    checked={formData?.mediums.includes(medium) || false}
                    onChange={() => handleMediumChange(medium)}
                  />
                  <span className="text-gray-600">{medium}</span>
                </label>
              ))}
            </div>
          ) : user.mediums && user.mediums.length > 0 ? (
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

        {isEditing && (
          <button onClick={() => void handleSave()} className="text-blue-500">
            Save
          </button>
        )}
      </div>
    </div>
  );
}
