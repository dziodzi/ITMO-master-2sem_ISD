import { useEffect, useRef } from "react";
import css from "./Feature.module.scss"; 

export const Features = () => {
  const featureRefs = useRef<HTMLDivElement[]>([]);

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add(css.visible); 
          } else {
            entry.target.classList.remove(css.visible);
          }
        });
      },
      {
        threshold: 0.2,
      }
    );

    featureRefs.current.forEach((el) => {
      if (el) observer.observe(el);
    });

    return () => observer.disconnect();
  }, []);

  const setFeatureRef = (el: HTMLDivElement | null, index: number) => {
    if (el) featureRefs.current[index] = el;
  };

  return (
    <section className={css.features}> 
      <h2>Наши преимущества</h2>
      <div className={css.featureList}>
        {[
          {
            title: "Высокая точность",
            text: "Модель показывает более 98% точности при детекции дипфейков",
            icon: "/icons/accuracy.svg",
            side: "left",
          },
          {
            title: "Мгновенная проверка",
            text: "Вы получаете результат почти сразу — за считаные секунды",
            icon: "/icons/speed.svg",
            side: "right",
          },
          {
            title: "Интеллектуальная аналитика",
            text: "вы получаете вероятность подделки в процентах — чтобы понимать, насколько фото вызывает сомнения",
            icon: "/icons/security.svg",
            side: "left",
          },
          {
            title: "Кроссплатформенность",
            text: "Удобно пользоваться как на компьютере, так и с телефона — неважно, где вы находитесь",
            icon: "/icons/cross-platform.svg",
            side: "right",
          },
        ].map((feature, index) => (
          <div
            key={index}
            className={`${css.featureItem} ${css[feature.side]}`} 
            ref={(el) => setFeatureRef(el, index)}
          >
            <div className={css.icon}>
              <img src={feature.icon} alt={feature.title} />
            </div>
            <div className={css.text}>
              <h3>{feature.title}</h3>
              <p>{feature.text}</p>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
};
