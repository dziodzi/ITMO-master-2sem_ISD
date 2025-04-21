import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { Landing } from './pages/Landing/Landing.tsx'
import { ResultPage} from './pages/Result/Result.tsx'

const router = createBrowserRouter([
  { path: "/", element: <Landing /> },
  { path: "/result", element: <ResultPage /> },
]);

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router}/>
  </StrictMode>,
)
