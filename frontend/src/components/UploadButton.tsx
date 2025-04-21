import { useNavigate } from "react-router-dom";
import { validationStore } from "../store/validation";
import css from "./UploadButton.module.scss"

export const UploadButton = () => {
  const navigate = useNavigate();

  const handleUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      validationStore.validate(file).then(() => {
        navigate("/result");
      });
      navigate("/loading");
    }
  };

  return (
    <div className={css.uploadWrapper}>
      <label className={css.uploadButton}>
        Загрузить фото
        <input type="file" accept="image/*" onChange={handleUpload} hidden />
      </label>
    </div>
  );
};
