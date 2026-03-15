import { useEffect, useState } from "react";

export default function App() {
  const [message, setMessage] = useState("Načítavam odpoveď zo servera...");
  const [error, setError] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/")
      .then((response) => {
        if (!response.ok) {
          throw new Error("Backend neodpovedal správne.");
        }
        return response.text();
      })
      .then((data) => {
        setMessage(data);
      })
      .catch((err) => {
        setError(err.message);
      });
  }, []);

  return (
    <div style={{ padding: "30px", fontFamily: "Arial" }}>
      <h1>React ↔ Spring Boot test!</h1>

      {error ? <p>Chyba: {error}</p> : <p>Odpoveď zo servera: {message}</p>}
    </div>
  );
}

