import React, { useState, useEffect, useCallback } from 'react';
import './styles/App.css';

import CurrencyForm from './components/CurrencyForm';
import ResultDisplay from './components/ResultDisplay';
import HistoryTable from './components/HistoryTable';
import FormatSelector from './components/FormatSelector';
import DownloadButtons from './components/DownloadButtons';
import { fetchHistory, convertCurrency, deleteExchange, updateTimestamp } from './services/currencyService';

function App() {
  const [from, setFrom] = useState('USD');
  const [to, setTo] = useState('BRL');
  const [amount, setAmount] = useState(1);
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [history, setHistory] = useState([]);
  const [formato, setFormato] = React.useState('json');

  const loadHistory = useCallback(async () => {
    try {
      const data = await fetchHistory(formato);
      setHistory(data);
    } catch (err) {
      setError('Erro ao buscar o histórico.');
    }
  }, [formato]);

  const handleConvert = async (formatoSelecionado = 'json') => {
    try {
      const res = await convertCurrency(from, to, amount, formato);
      setResult(res);
      setError('');
      await loadHistory();
    } catch (err) {
      setError('Erro ao converter moedas.');
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteExchange(id);
      await loadHistory(formato);
    } catch (err) {
      setError('Erro ao excluir entrada.');
    }
  };

const handleSaveTimestamp = async (id, newTimestamp) => {
  try {
    await updateTimestamp(id, newTimestamp);
    await loadHistory(formato); // recarrega o histórico atualizado do backend
  } catch (err) {
    setError('Erro ao atualizar data/hora.');
  }
};

  useEffect(() => {
    loadHistory();
  }, [loadHistory]);

  return (
    <div className="App">
      <center>
      <h1>Conversor de Moedas</h1>
      <FormatSelector formato={formato} setFormato={setFormato} />
      <CurrencyForm
        from={from}
        to={to}
        amount={amount}
        setFrom={setFrom}
        setTo={setTo}
        setAmount={setAmount}
        onConvert={() => handleConvert(formato)}
      />
      {error && <p className="error">{error}</p>}
      <ResultDisplay result={result} />
      <DownloadButtons />
      <HistoryTable history={history} onDelete={handleDelete} onSaveTimestamp={handleSaveTimestamp} />
      </center>
    </div>
    
  );
}

export default App;
