import axios from 'axios';
import { parseXmlConvertResponse, parseXmlHistory } from './xmlParsers';

// Importa o parser XML (para converter XML em JSON)
import { XMLParser } from 'fast-xml-parser';

const API_BASE = 'http://192.168.1.78:9080/v1/currency';

// Instancia um parser XML reutilizável
const xmlParser = new XMLParser();

export const obterHistorico = async (formato:string) => {
  const response = await axios.get(`${API_BASE}/history?formato=${formato}`, {
    headers: { Accept: formato === 'xml' ? 'application/xml' : 'application/json' },
    responseType: formato === 'xml' ? 'text' : 'json', // necessário para tratar XML como texto
  });

  // Se for XML, converte para objeto JavaScript usando o parser
  if (formato === 'xml') {
    const jsonObj = xmlParser.parse(response.data);
    const parsed = parseXmlHistory(jsonObj); // Normaliza a estrutura do XML
    return parsed;
  }

  return response.data; // JSON puro
};

export const convertCurrency = async (from:string, to:string, amount:string, formato = "json") =>{
  const response = await axios.post(
    `${API_BASE}/convert?formato=${formato}`,
    { from, to, amount }, // corpo da requisição POST
    {
      headers: { Accept: formato === 'xml' ? 'application/xml' : 'application/json' },
      responseType: formato === 'xml' ? 'text' : 'json',
    }
  );

  // Se XML, faz o parse; senão retorna JSON diretamente
  return formato === 'xml'
    ? parseXmlConvertResponse(xmlParser.parse(response.data))
    : response.data;
};

export const apagarConversaoo = async (id:number, formato = 'json') => {
  await axios.delete(`${API_BASE}/delete-exchange?formato=${formato}`, {
    params: { idExchange: id },
    headers: { Accept: formato === 'xml' ? 'application/xml' : 'application/json' },
    responseType: formato === 'xml' ? 'text' : 'json',
  });
};

export const atualizarTimestamp = async (id:number, newTimestamp:string, formato = 'json') => {
  await axios.put(
    `${API_BASE}/update-timestamp?id=${id}&newTimestamp=${newTimestamp}&formato=${formato}`,
    null, // Sem corpo na requisição PUT
    {
      headers: { Accept: formato === 'xml' ? 'application/xml' : 'application/json' },
      responseType: formato === 'xml' ? 'text' : 'json',
    }
  );
};

