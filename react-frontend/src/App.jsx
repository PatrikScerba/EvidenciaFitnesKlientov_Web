import { useEffect, useState } from "react";
import { getCurrentUser } from "./api/authApi";

export default function App() {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        async function loadCurrentUser() {
            try {
                const currentUser = await getCurrentUser();
                setUser(currentUser);
            } catch (err) {
                setUser(null);
                setError("");
            } finally {
                setLoading(false);
            }
        }

        loadCurrentUser();
    }, []);

    if (loading) {
        return (
            <div style={{ padding: "30px", fontFamily: "Arial" }}>
                <h1>React ↔ Spring Boot</h1>
                <p>Načítavam prihlásenie...</p>
            </div>
        );
    }

    return (
        <div style={{ padding: "30px", fontFamily: "Arial" }}>
            <h1>React ↔ Spring Boot</h1>

            {user ? (
                <div>
                    <p>Používateľ je prihlásený.</p>
                    <pre>{JSON.stringify(user, null, 2)}</pre>
                </div>
            ) : (
                <div>
                    <p>Používateľ nie je prihlásený.</p>
                    {error && <p>Chyba: {error}</p>}
                </div>
            )}
        </div>
    );
}

