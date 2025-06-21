// import React from 'react';

// const HistoryTable = ({ history, onDelete }) => (
//   <div>
//     <h2>Histórico de Conversões</h2>
//     <table className="history-table">
//       <thead>
//         <tr>
//           <th>De</th>
//           <th>Para</th>
//           <th>Valor</th>
//           <th>Convertido</th>
//           <th>Data/Hora</th>
//           <th>Ações</th>
//         </tr>
//       </thead>
//       <tbody>
//         {history.map((entry) => (
//           <tr key={entry.id}>
//             <td>{entry.fromCurrency}</td>
//             <td>{entry.toCurrency}</td>
//             <td>{entry.amount}</td>
//             <td>{entry.convertedAmount}</td>
//             <td>{new Date(entry.timestamp).toLocaleString()}</td>
//             <td>
//               <button className="delete-button" onClick={() => onDelete(entry.id)}>Excluir</button>
//             </td>
//           </tr>
//         ))}
//       </tbody>
//     </table>
//   </div>
// );

// export default HistoryTable;

import React, { useState } from 'react';

const HistoryTable = ({ history, onDelete, onSaveTimestamp }) => {
  const [editId, setEditId] = useState(null);
  const [editTimestamp, setEditTimestamp] = useState('');

  const handleEditClick = (entry) => {
    setEditId(entry.id);
    // Converte o timestamp para string compatível com input datetime-local
    const dt = new Date(entry.timestamp);
    const isoString = dt.toISOString().slice(0,16); // "YYYY-MM-DDTHH:mm"
    setEditTimestamp(isoString);
  };

  const handleSaveClick = () => {
    onSaveTimestamp(editId, editTimestamp);
    setEditId(null);
  };

  const handleCancelClick = () => {
    setEditId(null);
  };

  return (
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
              <td>
                {editId === entry.id ? (
                  <input
                    type="datetime-local"
                    value={editTimestamp}
                    onChange={(e) => setEditTimestamp(e.target.value)}
                  />
                ) : (
                  new Date(entry.timestamp).toLocaleString()
                )}
              </td>
              <td>
                {editId === entry.id ? (
                  <>
                    <button onClick={handleSaveClick}>Salvar</button>
                    <button onClick={handleCancelClick}>Cancelar</button>
                  </>
                ) : (
                  <>
                    <button onClick={() => handleEditClick(entry)}>Editar</button>
                    <button onClick={() => onDelete(entry.id)}>Excluir</button>
                  </>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default HistoryTable;
