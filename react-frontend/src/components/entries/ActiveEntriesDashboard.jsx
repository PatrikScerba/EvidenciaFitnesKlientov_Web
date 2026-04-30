import { useEffect, useState } from "react";
import { getActiveEntries, registerDeparture } from "../../api/entryApi";

export default function ActiveEntriesDashboard() {
  const [entries, setEntries] = useState([]);

  async function loadEntries() {
    const data = await getActiveEntries();
    setEntries(data);
  }

  useEffect(() => {
    loadEntries();
  }, []);

  async function handleDeparture(clientId) {
    await registerDeparture(clientId);
    await loadEntries();
  }

  return (
    <div>
      <h3>Aktuálne vo fitku</h3>

      {entries.length === 0 ? (
        <p>Momentálne nie je v fitnescentre žiadny klient.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Meno</th>
              <th>Priezvisko</th>
              <th>Dátum</th>
              <th>Príchod</th>
              <th>Akcia</th>
            </tr>
          </thead>

          <tbody>
            {entries.map((entry) => (
              <tr key={entry.id}>
                <td>{entry.firstName}</td>
                <td>{entry.lastName}</td>
                <td>{entry.date}</td>
                <td>{entry.arrivalTime}</td>
                <td>
                  <button onClick={() => handleDeparture(entry.clientId)}>
                    Ukončiť vstup
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
