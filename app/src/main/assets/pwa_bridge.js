// Вставляется в PWA при загрузке (можно вставить в index.html)
window.MeshAgent = window.MeshAgent || {};
window.MeshAgent.saveTraining = function(json) {
  if (window.AndroidBridge) window.AndroidBridge.saveTrainingData(JSON.stringify(json));
};
window.MeshAgent.startTraining = function() {
  if (window.AndroidBridge) window.AndroidBridge.startTraining();
};
window.MeshAgent.bootstrap = function() {
  if (window.AndroidBridge) window.AndroidBridge.bootstrapTermux();
};
window.MeshAgent.onBootstrap = function(ok) {
  console.log("Bootstrap result:", ok);
};
