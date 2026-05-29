import { useState } from "react";
import { getSecurityQuestion, resetClientQrToken } from "../../api/adminApi";

const securityQuestionLabels = {
  PET_NAME: "Ako sa volal tvoj prvý domáci miláčik?",
  BIRTH_CITY: "V akom meste si sa narodil?",
  CHILDHOOD_NICKNAME: "Aká bola tvoja prezývka z detstva?",
  FAVORITE_TEACHER: "Ako sa volal tvoj obľúbený učiteľ?",
  STREET_GROWING_UP: "Ako sa volala ulica, na ktorej si vyrastal?",
  FIRST_SCHOOL: "Ako sa volala tvoja prvá škola?",
  MOTHER_MAIDEN_NAME: "Aké bolo rodné priezvisko tvojej mamy?",
  FAVORITE_SINGER_CHILDHOOD: "Kto bol tvoj obľúbený spevák v detstve?",
  FIRST_CAR: "Aká bola značka tvojho prvého auta?",
  DREAM_JOB_CHILDHOOD: "Aké bolo tvoje vysnívané povolanie v detstve?",
};

export default function AdminQrTokenReset() {
  const [email, setEmail] = useState("");
  const [securityQuestion, setSecurityQuestion] = useState("");
  const [securityAnswer, setSecurityAnswer] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [questionLoaded, setQuestionLoaded] = useState(false);

  async function handleLoadQuestion(e) {
    e.preventDefault();

    setError("");
    setMessage("");
    setSecurityQuestion("");
    setSecurityAnswer("");
    setQuestionLoaded(false);

    try {
      setLoading(true);

      const response = await getSecurityQuestion(email);
      setSecurityQuestion(response.securityQuestion);
      setQuestionLoaded(true);
    } catch (err) {
      setError(err.message || "Nepodarilo sa načítať bezpečnostnú otázku.");
    } finally {
      setLoading(false);
    }
  }

  async function handleResetQrToken(e) {
    e.preventDefault();

    setError("");
    setMessage("");

    try {
      setLoading(true);

      const response = await resetClientQrToken({
        email,
        securityAnswer,
      });

      setMessage(response.message || "QR token bol úspešne resetovaný.");
      setSecurityAnswer("");
    } catch (err) {
      setError(err.message || "Nepodarilo sa resetovať QR token.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ padding: "30px", maxWidth: "500px" }}>
      <h2>Reset QR tokenu klienta</h2>

      <form onSubmit={handleLoadQuestion}>
        <div style={{ marginBottom: "15px" }}>
          <label htmlFor="email">Email klienta</label>
          <br />
          <input
            id="email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Zadaj email klienta"
            style={{ width: "100%", padding: "8px" }}
            required
          />
        </div>

        <button type="submit" disabled={loading}>
          {loading ? "Načítavam..." : "Načítať otázku"}
        </button>
      </form>

      {questionLoaded && (
        <form onSubmit={handleResetQrToken} style={{ marginTop: "25px" }}>
          <div style={{ marginBottom: "15px" }}>
            <label>Bezpečnostná otázka</label>
            <div
              style={{
                marginTop: "8px",
                padding: "10px",
                border: "1px solid #ccc",
                borderRadius: "6px",
                backgroundColor: "#f8f8f8",
              }}
            >
              {securityQuestionLabels[securityQuestion] || securityQuestion}
            </div>
          </div>

          <div style={{ marginBottom: "15px" }}>
            <label htmlFor="securityAnswer">Bezpečnostná odpoveď</label>
            <br />
            <input
              id="securityAnswer"
              type="password"
              value={securityAnswer}
              onChange={(e) => setSecurityAnswer(e.target.value)}
              placeholder="Zadaj odpoveď"
              style={{ width: "100%", padding: "8px" }}
              required
            />
          </div>

          <button type="submit" disabled={loading}>
            {loading ? "Resetujem..." : "Resetovať QR token"}
          </button>
        </form>
      )}

      {message && (
        <p style={{ marginTop: "20px", color: "green" }}>{message}</p>
      )}

      {error && <p style={{ marginTop: "20px", color: "red" }}>{error}</p>}
    </div>
  );
}


