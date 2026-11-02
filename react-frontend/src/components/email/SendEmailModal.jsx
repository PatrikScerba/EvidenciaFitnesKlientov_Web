import useEmailForm from "../../hooks/useEmailForm";
import { useEffect, useState } from "react";
import { getAllClients } from "../../api/clientApi";

export default function SendEmailModal({
  client,
  onClose,
  mode = "single",
  sendToAll = false,
  selectedClientIds = [],
  selectedClients = [],
}) {
  const {
    emailData,
    error,
    isSending,
    handleChange,
    attachmentInputRef,
    handleAttachmentsChange,
    handleSubmit,
  } = useEmailForm();

  const [allRecipients, setAllRecipients] = useState([]);
  const [recipientsLoading, setRecipientsLoading] = useState(false);
  const [recipientsError, setRecipientsError] = useState("");
  const [showAllRecipients, setShowAllRecipients] = useState(false);

  const isSelectedMode = mode === "selected";
  const isAllMode = mode === "all";

  useEffect(() => {
    if (!isAllMode) return;

    let cancelled = false;

    async function fetchRecipients() {
      setRecipientsLoading(true);
      setRecipientsError("");

      try {
        const clients = await getAllClients();

        if (!cancelled) {
          setAllRecipients(clients);
        }
      } catch (err) {
        if (!cancelled) {
          setRecipientsError(
            err.message || "Nepodarilo sa načítať zoznam klientov."
          );
        }
      } finally {
        if (!cancelled) {
          setRecipientsLoading(false);
        }
      }
    }

    fetchRecipients();

    return () => {
      cancelled = true;
    };
  }, [isAllMode]);

  const onSubmit = async (event) => {
    event.preventDefault();

    if (isSelectedMode && selectedClientIds.length === 0) return;

    const clientIds = isAllMode
      ? []
      : isSelectedMode
        ? selectedClientIds
        : client.clientId;

    await handleSubmit(clientIds, isAllMode);
  };

  return (
    <div style={{ padding: "20px", border: "1px solid #ccc" }}>
      <h3>Odoslať email klientovi</h3>

      {isAllMode ? (
        <div>
          <p>Príjemcovia: Všetci klienti</p>
          {recipientsLoading && <p>Načítavam zoznam klientov...</p>}
          {recipientsError && (
            <p style={{ color: "blue" }}>{recipientsError}</p>
          )}

          {!recipientsLoading && !recipientsError && (
            <div>
              <p>Počet evidovaných klientov: {allRecipients.length}</p>

              {allRecipients.length > 0 && (
                <button
                  type="button"
                  onClick={() => setShowAllRecipients((prev) => !prev)}
                >
                  {showAllRecipients
                    ? "Skryť zoznam príjemcov"
                    : "Zobraziť zoznam príjemcov"}
                </button>
              )}

              {showAllRecipients && allRecipients.length > 0 && (
                <ul
                  style={{
                    maxHeight: "250px",
                    overflowY: "auto",
                  }}
                >
                  {allRecipients.map((client) => (
                    <li key={client.clientId}>
                      {client.firstName} {client.lastName} ({client.email})
                    </li>
                  ))}
                </ul>
              )}
            </div>
          )}
        </div>
      ) : isSelectedMode ? (
        <div>
          <p>Príjemcovia: {selectedClients.length} vybraných klientov</p>

          <ul>
            {selectedClients.map((client) => (
              <li key={client.clientId}>
                {client.firstName} {client.lastName} ({client.email})
              </li>
            ))}
          </ul>
        </div>
      ) : (
        <p>
          Príjemca: {client.firstName} {client.lastName} ({client.email})
        </p>
      )}
      <form onSubmit={onSubmit}>
        <div>
          <label>Predmet</label>
          <input
            type="text"
            name="subject"
            value={emailData.subject}
            onChange={handleChange}
            maxLength={150}
            required
          />
        </div>

        <div>
          <label>Správa</label>
          <textarea
            name="message"
            value={emailData.message}
            onChange={handleChange}
            maxLength={5000}
            rows={6}
          />
        </div>

        <div>
          <label>Prílohy</label>
          <input
            type="file"
            multiple
            ref={attachmentInputRef}
            onChange={handleAttachmentsChange}
          />
        </div>

        {error && <p style={{ color: "red" }}>{error}</p>}

        <button
          type="submit"
          disabled={
            isSending || (isSelectedMode && selectedClientIds.length === 0)
          }
        >
          {isSending ? "Odosielam..." : "Odoslať email"}
        </button>

        <button type="button" onClick={onClose} disabled={isSending}>
          Zrušiť
        </button>
      </form>
    </div>
  );
}
