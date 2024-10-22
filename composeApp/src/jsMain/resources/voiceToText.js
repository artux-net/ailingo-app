let recognition = null;
let isListening = false;

let recognitionCallback = null;
let listeningCallback = null;

function initRecognition() {
  if (!recognition) {
    recognition = new webkitSpeechRecognition();
    recognition.onresult = function(event) {
      const result = event.results[0][0].transcript;
      if (recognitionCallback && typeof recognitionCallback === 'function') {
        recognitionCallback(result);
      }
    };

    recognition.onerror = function(event) {
      console.error('Ошибка при распознавании речи:', event.error);
    };

    recognition.onstart = function() {
      isListening = true;
      if (listeningCallback && typeof listeningCallback === 'function') {
        listeningCallback(isListening);
      }
    };

    recognition.onend = function() {
      isListening = false;
      if (listeningCallback && typeof listeningCallback === 'function') {
        listeningCallback(isListening);
      }
    };
  }
}

export function setRecognitionCallback(callback) {
  recognitionCallback = callback;
}

export function setListeningCallback(callback) {
  listeningCallback = callback;
}

export function startListening() {
  initRecognition();
  recognition.start();
}

export function stopListening() {
  if (recognition) {
    recognition.stop();
  } else {
    console.error('Recognition не был инициализирован.');
  }
}
