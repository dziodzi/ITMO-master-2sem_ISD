import { useState, useEffect } from "react";
import Logo from './Logo.svg?react';
import css from "./Header.module.scss"

export const Header = () => {
  const [showHeader, setShowHeader] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      const heroHeight = document.querySelector(".hero")?.clientHeight || 300;
      setShowHeader(window.scrollY > heroHeight - 50);
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return showHeader ? 
        <header className={css.Header}>
          <div className={css.logo}><Logo/></div>
          <div className={css.actions}>
            <button className={css.login}>Войти</button>
            <button className={css.register}>Регистрация</button>
          </div>
        </header> : <></>

  
};
