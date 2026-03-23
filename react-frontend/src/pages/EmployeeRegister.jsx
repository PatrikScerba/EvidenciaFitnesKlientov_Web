import { useState } from "react";

export default function EmployeeRegister() {
  const [form, setForm] = useState({
    email: "",
  });

  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  function handleChange(e) {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setResult(null);
    setLoading(true);

    try {
      const response = await fetch("http://localhost:8080/api/admin/employees", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(form),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Registrácia zamestnanca zlyhala.");
      }

      const data = await response.json();
      setResult(data);

      setForm({
        email: "",
      });
    } catch (err) {
      setError(err.message || "Nastala chyba.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ padding: "30px", maxWidth: "500px" }}>
      <h2>Test registrácie zamestnanca</h2>

      <form onSubmit={handleSubmit}>
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
        />
        <br /><br />

        <button type="submit" disabled={loading}>
          {loading ? "Odosiela sa..." : "Registrovať zamestnanca"}
        </button>
      </form>

      {error && (
        <div style={{ marginTop: "20px", color: "red" }}>
          <strong>Chyba:</strong> {error}
        </div>
      )}

      {result && (
        <div style={{ marginTop: "20px" }}>
          <p><strong>Email:</strong> {result.email}</p>
          <p><strong>Dočasné heslo:</strong> {result.temporaryPassword}</p>
          <p><strong>Správa:</strong> {result.message}</p>
        </div>
      )}
    </div>
  );
}

