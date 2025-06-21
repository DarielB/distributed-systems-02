import React from 'react';

const HistoryTable = ({ history, onDelete }) => (
  <div>
    <h2>Histórico de Conversões</h2>
    <table className="history-table">
      <thead>
        <tr>
          <th>De</th>
          <th>Para</th>
          <th>Valor</th>
          <th>Convertido</th>
          <th>Data/Hora</th>
          <th>Ações</th>
        </tr>
      </thead>
      <tbody>
        {history.map((entry) => (
          <tr key={entry.id}>
            <td>{entry.fromCurrency}</td>
            <td>{entry.toCurrency}</td>
            <td>{entry.amount}</td>
            <td>{entry.convertedAmount}</td>
            <td>{new Date(entry.timestamp).toLocaleString()}</td>
            <td>
              <button className="delete-button" onClick={() => onDelete(entry.id)}>Excluir</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  </div>
);

export default HistoryTable;
