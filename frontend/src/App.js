import './App.scss';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage/HomePage';
import AuthPage from './pages/AuthPage/AuthPage';
import NotFound from './pages/NotFound/NotFound'
import Header from './containers/Header/Header';
import routes from './utils/routes.json'
import Market from './pages/Market/Market';
import Leaderboard from './pages/Leaderboard/Leaderboard';
import About from './pages/About/About';

function App() {
  return (
    <div className="App">
      <Router>
        <Header />
        <Routes>
          <Route path={routes.home} element={<HomePage />} />
          <Route path={routes.auth} element={<AuthPage />} />
          <Route path={routes.market} element={<Market />} />
          <Route path={routes.leaderboard} element={<Leaderboard />} />
          <Route path={routes.about} element={<About />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
