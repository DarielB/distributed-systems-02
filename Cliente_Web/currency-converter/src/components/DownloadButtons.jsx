import React from 'react';
import { downloadJson, downloadXml } from '../services/currencyService';

const DownloadButtons = () => {
  const handleDownload = async (format) => {
    try {
      let response;
      let filename;

      switch (format) {
        case 'json':
          response = await downloadJson();
          filename = 'exchange_history.json';
          break;
        case 'xml':
          response = await downloadXml();
          filename = 'exchange_history.xml';
          break;
        default:
          return;
      }

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (error) {
      console.error('Erro ao fazer download:', error);
    }
  };

  return (
    <div style={{ marginTop: '20px' }}>
      <h3>Baixar histórico</h3>
      <button onClick={() => handleDownload('json')}>Baixar JSON</button>{' '}
      <button onClick={() => handleDownload('xml')}>Baixar XML</button>{' '}
    </div>
  );
};

export default DownloadButtons;
