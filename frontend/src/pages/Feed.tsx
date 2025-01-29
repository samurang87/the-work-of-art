import { useEffect, useState } from "react";
import axios from "axios";

export default function Feed() {
    const [status, setStatus] = useState<string>('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        async function fetchStatus() {
            try {
                const response = await axios.get<string>('/api/status');
                setStatus(response.data);
            } catch (error) {
                setError(error instanceof Error ? error.message : 'An error occurred');
            } finally {
                setLoading(false);
            }
        }
        void fetchStatus();
    }, []);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div>
            <p>The status of the backend is: {status}</p>
        </div>
    );
}
