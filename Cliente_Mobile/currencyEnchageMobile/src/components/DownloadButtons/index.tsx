import React from 'react';
import * as FileSystem from 'expo-file-system';
import { Text, View, StyleSheet, Pressable, Alert } from 'react-native';
import { shareAsync } from 'expo-sharing';

const DownloadButtons = () => {

    const handleDownload = async (format: string) => {
        try {
            let uri = "http://192.168.1.78:9080/v1/currency/download";
            let accept;
            let filename;

            switch (format) {
                case 'json':
                    accept = "application/json";
                    filename = 'exchange_history.json';
                    break;
                case 'xml':
                    accept = "application/xml";
                    filename = 'exchange_history.xml';
                    break;
                case 'protobuf':
                    uri = "http://192.168.1.78:9080/v1/currency/download-protobuf";
                    filename = 'exchange_history.pb';
                    accept = "application/x-protobuf";
                    break;
                default:
                    return;
            }

            const fileUri = FileSystem.documentDirectory + filename;

            try {
                const downloadResumable = FileSystem.createDownloadResumable(
                    uri,
                    fileUri,
                    {
                        headers: { accept: accept },
                    }
                );

                const result = await downloadResumable.downloadAsync();

                if (result?.uri != undefined) {
                    shareAsync(result.uri);
                }

            } catch (error) {
                console.error('Erro no download:', error);
                Alert.alert('Erro', 'Falha no download');
            }
        } catch (error) {
            console.error('Erro no download:', error);
            Alert.alert('Erro', 'Falha no download');
        }
    }

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Baixar histórico</Text>

            <View style={styles.buttonContainer}>
                <Pressable style={styles.button} onPress={() => handleDownload('json')}>
                    <Text style={styles.buttonText}>Baixar JSON</Text>
                </Pressable>

                <Pressable style={styles.button} onPress={() => handleDownload('xml')}>
                    <Text style={styles.buttonText}>Baixar XML</Text>
                </Pressable>

                <Pressable style={styles.button} onPress={() => handleDownload('protobuf')}>
                    <Text style={styles.buttonText}>Baixar Protobuf</Text>
                </Pressable>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        marginTop: 20,
        alignItems: 'center',
    },
    title: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 10,
    },
    buttonContainer: {
        flexDirection: 'row',
        justifyContent: 'center',
        gap: 10, // Se não funcionar na sua versão, use marginRight nos botões
        flexWrap: 'wrap', // Para quebrar linha se não couber na tela
    },
    button: {
        backgroundColor: '#4472c4',
        paddingVertical: 10,
        paddingHorizontal: 16,
        borderRadius: 8,
    },
    buttonText: {
        color: '#fff',
        fontSize: 14,
    },
});

export default DownloadButtons;
