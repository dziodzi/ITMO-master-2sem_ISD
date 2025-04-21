import { observer } from "mobx-react-lite";
import { validationStore } from "../../store/validation";
import { useNavigate } from "react-router-dom";
// import "../styles/result.scss";

export const ResultPage = observer(() => {
  const navigate = useNavigate();
  const { file, result, probability } = validationStore;

  if (!file || result === null || probability === null) {
    navigate("/");
    return null;
  }

  const imageURL = URL.createObjectURL(file);
  const isFake = result === "fake";

  return (
    <div className="result">
      <h2>Результат анализа</h2>
      <img src={imageURL} alt="Uploaded" />
      <p className={`status ${isFake ? "fake" : "real"}`}>
        {isFake ? "Фейк" : "Оригинал"} ({(probability * 100).toFixed(1)}%)
      </p>
      <button onClick={() => { validationStore.reset(); navigate("/"); }}>
        Загрузить ещё
      </button>
    </div>
  );
});
