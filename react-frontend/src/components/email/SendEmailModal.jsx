import useEmailForm from "../../hooks/useEmailForm";
import { useEffect, useRef, useState } from "react";
import { getAllClients } from "../../api/clientApi";

export default function SendEmailModal({
  client,
  onClose,
  onSuccess,
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

  const sendingRef = useRef(false);

  const effectiveMode =
    mode === "selected"
      ? "selected"
      : mode === "all" || sendToAll
        ? "all"
        : "single";

  const isSelectedMode = effectiveMode === "selected";
  const isAllMode = effectiveMode === "all";
  const isSingleMode = effectiveMode === "single";

  useEffect(() => {
    if (!isAllMode) return;

    let cancelled = false;

    async function fetchRecipients() {
      setRecipientsLoading(true);
      setRecipientsError("");

      try {
        const response = await getAllClients();

        const clients = Array.isArray(response) ? response : response?.content;

        if (!Array.isArray(clients)) {
          throw new Error("Neplatný formát zoznamu klientov.");
        }

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

  const sendEmailOnce = async (clientIds, sendToAll) => {
    if (sendingRef.current) return;

    sendingRef.current = true;

    try {
      const success = await handleSubmit(clientIds, sendToAll);

      if (success) {
        onSuccess?.();
      }
    } finally {
      sendingRef.current = false;
    }
  };

  const onSubmit = async (event) => {
    event.preventDefault();

    if (isSending || sendingRef.current) return;

    if (isSelectedMode && selectedClientIds.length === 0) {
      return;
    }

    if (
      isAllMode &&
      (recipientsLoading || recipientsError || allRecipients.length === 0)
    ) {
      return;
    }

    if (isSingleMode && !client?.clientId) {
      return;
    }

    const clientIds = isAllMode
      ? []
      : isSelectedMode
        ? [...selectedClientIds]
        : client.clientId;

    await sendEmailOnce(clientIds, isAllMode);
  };

  return (
    <div
      style={{
        padding: "20px",
        border: "1px solid #ccc",
      }}
    >
      <h3>Odoslať email klientovi</h3>

      {isAllMode ? (
        <div>
          <p>Príjemcovia: Všetci klienti</p>

          {recipientsLoading && <p>Načítavam zoznam klientov...</p>}

          {recipientsError && <p style={{ color: "red" }}>{recipientsError}</p>}

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
                  {allRecipients.map((recipient) => (
                    <li key={recipient.clientId}>
                      {recipient.firstName} {recipient.lastName} (
                      {recipient.email})
                    </li>
                  ))}
                </ul>
              )}
            </div>
          )}
        </div>
      ) : isSelectedMode ? (
        /* VYBRANÍ KLIENTI */
        <div>
          <p>Príjemcovia: {selectedClientIds.length} vybraných klientov</p>

          <ul>
            {selectedClients.map((recipient) => (
              <li key={recipient.clientId}>
                {recipient.firstName} {recipient.lastName} ({recipient.email})
              </li>
            ))}
          </ul>
        </div>
      ) : (
        <p>
          Príjemca: {client?.firstName} {client?.lastName} ({client?.email})
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
            isSending ||
            (isSingleMode && !client?.clientId) ||
            (isSelectedMode && selectedClientIds.length === 0) ||
            (isAllMode &&
              (recipientsLoading ||
                !!recipientsError ||
                allRecipients.length === 0))
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
