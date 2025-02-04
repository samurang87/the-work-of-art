import {useEffect, useState} from "react";
import {User, WorkOfArt} from "../types.tsx";
import axios from "axios";

type WorkOfArtComponentProps = {
    workOfArtId?: string;
};

function transformImageUrl(url: string): string {
    const parts = url.split('/upload/');
    if (parts.length !== 2) {
        throw new Error('Invalid URL format');
    }
    return `${parts[0]}/upload/c_scale,w_500/q_auto/f_auto/${parts[1]}`;
}

function formatDate(dateString: string): string {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}

function useUser(userId: string | undefined) {
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!userId) {
            setLoading(false);
            return;
        }

        async function fetchUser() {
            setLoading(true);
            try {
                const response = await axios.get<User>('/api/user/', {
                    params: {id: userId}
                });
                setUser(response.data);
                setError(null);
            } catch (error) {
                setError(error instanceof Error ? error.message : String(error));
            } finally {
                setLoading(false);
            }
        }

        void fetchUser();
    }, [userId]);

    return {user, loading, error};
}

export default function WorkOfArtComponent({
                                               workOfArtId,
                                               allFields = true
                                           }: WorkOfArtComponentProps & {
    allFields?: boolean
}) {
    const [workOfArt, setWorkOfArt] = useState<WorkOfArt | null>(null);
    const [workOfArtLoading, setWorkOfArtLoading] = useState(true);
    const [workOfArtError, setWorkOfArtError] = useState<string | null>(null);
    const {user, loading: userLoading, error: userError} = useUser(workOfArt?.user);

    useEffect(() => {
        async function fetchWorkOfArt(workOfArtId: string | undefined) {
            if (!workOfArtId) {
                setWorkOfArtError('Work of art ID is required');
                setWorkOfArtLoading(false);
                return;
            }
            setWorkOfArtLoading(true);  // Reset loading when workOfArtId changes
            try {
                const response = await axios.get<WorkOfArt>('/api/woa/' + workOfArtId);
                setWorkOfArt(response.data);
                setWorkOfArtError(null);
            } catch (error) {
                setWorkOfArtError(error instanceof Error ? error.message : String(error));
            } finally {
                setWorkOfArtLoading(false);
            }
        }

        void fetchWorkOfArt(workOfArtId);
    }, [workOfArtId]);

    if (workOfArtLoading || userLoading) return <div
        className="mt-24 text-center">Loading...</div>;
    if (workOfArtError || userError) return <div
        className="mt-24 text-center text-red-500">Error: {workOfArtError}</div>;
    if (!workOfArt || !user) return <div className="mt-24 text-center">No work of art
        data
        available</div>;

    return (
        <div className="container mx-auto px-4 mt-24">
            <div className="max-w-2xl mx-auto space-y-6">
                {/* User and Medium Section */}
                <div
                    className="flex justify-between bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
                    <div>
                        <h2 className="text-2xl font-semibold text-gray-800">
                            <a href={`/user/${user.name}`}>{user.name}</a>
                        </h2>
                    </div>
                    <div>
                        <p className="text-gray-600">{workOfArt.medium}</p>
                    </div>
                </div>

                {/* Image Section */}
                <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
                    <img src={transformImageUrl(workOfArt.imageUrl)}
                         alt={workOfArt.title} className="w-full rounded-lg"/>
                </div>

                {/* Title Section */}
                <div
                    className="flex justify-between bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
                    <div>
                        <h3 className="text-2xl font-semibold text-gray-800">
                            <a href={`/woa/${workOfArt.id}`}>{workOfArt.title}</a>
                        </h3>
                    </div>
                    <div>
                        <p className="text-gray-600">{formatDate(workOfArt.createdAt)}</p>
                    </div>
                </div>
                {allFields && (
                    <>
                        {/* Description Section */}
                        <div
                            className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
                            <h3 className="text-xl font-semibold text-gray-800 mb-4">Description</h3>
                            <p className="text-gray-600">
                                {workOfArt.description || 'No description yet 🌳'}
                            </p>
                        </div>

                        {/* Materials Section */}
                        {workOfArt.materials && workOfArt.materials.length > 0 && (
                            <div
                                className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
                                <h3 className="text-xl font-semibold text-gray-800 mb-4">Materials</h3>
                                {workOfArt.materials.map((material, index) => (
                                    <p key={index} className="text-gray-600">
                                        {/* TODO: Different material description depending on what's available */}
                                        {material.type} {material.identifier} - {material.name} by {material.brand} ({material.line})
                                    </p>
                                ))}
                            </div>
                        )}

                        {/* Challenge Section */}
                        {workOfArt.challengeId && (
                            <div
                                className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
                                <h3 className="text-xl font-semibold text-gray-800 mb-4">Challenge</h3>
                                <p className="text-gray-600">
                                    See challenge: {workOfArt.challengeId}
                                </p>
                            </div>
                        )}
                    </>
                )}
            </div>
        </div>
    );
}
