import css from "./Landing.module.scss";
import { UploadButton } from "../../components/UploadButton";
import { Header } from "../../components/Header/Header";
import { Features } from "../../components/Features/Features";
import { StarsContainer } from "../../components/StarsContainer/StarsContainer";

export const Landing = () => {
  return (
    <div className={css.landing}>
      <Header />
      <section className={css.hero}>
        <StarsContainer />
        <div className={css.title}>
          <h1>I-Dentity</h1>
          <p>
            AI-платформа для валидации изображений и выявления дипфейков с
            высокой точностью
          </p>
        </div>
        <div className={css.uploadWrapper}>
          <UploadButton />
        </div>
      </section>

      <section className={css.about}>
        <h2>О нас</h2>
        <p>
          I-Dentity — это система валидации изображений нового поколения. Мы
          используем передовые методы машинного обучения для анализа фотографий
          и определения их подлинности. Наша модель обучена на большом
          количестве дипфейков и реальных изображений, что позволяет достигать
          точности более 98%. 
        </p>
      </section>

      <section className={css.pipeline}>
        <h2>Как это работает</h2>
        <div className={css.steps}>
          <div className={css.step}>
            <h3>1</h3>
            <p>Загрузка изображения</p>
          </div>
          <div className={css.step}>
            <h3>2</h3>
            <p>Анализ с помощью AI-модели</p>
          </div>
          <div className={css.step}>
            <h3>3</h3>
            <p>Вывод результата</p>
          </div>
        </div>
      </section>
      <Features />
      <footer className={css.footer}>
        <StarsContainer />
        <p>© 2025 I-Dentity</p>
      </footer>
    </div>
  );
};
