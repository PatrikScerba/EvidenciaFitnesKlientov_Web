import { useRef, useState } from "react";

export default function useEmailForm() {
  const [emailData, setEmailData] = useState({
    subject: "",
    message: "",
  });

  const [attachments, setAttachments] = useState([]);
  const [error, setError] = useState("");
  const [isSending, setIsSending] = useState(false);

  const attachmentInputRef = useRef(null);

  return {
    emailData,
    attachments,
    error,
    isSending,
    attachmentInputRef,
  };
}
