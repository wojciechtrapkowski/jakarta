/**
 * Handles incoming push messages from JSF push channels.
 * This function is called when a message is received on either the broadcast or user channel.
 * 
 * @param message The message object received from the push channel
 */
function handlePushMessage(message) {
    console.log('Push message received:', message);
    
    // Display the message in the chat interface
    displayChatMessage(message);
}

/**
 * Displays a chat message in the messages container.
 * 
 * @param message The message object to display
 */
function displayChatMessage(message) {
    var messagesDiv = document.getElementById('messages');
    if (!messagesDiv) {
        console.warn('Messages container not found');
        return;
    }
    
    var messageElement = document.createElement('div');
    messageElement.className = 'card mb-2';
    
    // Get current user ID from the page
    var currentUserId = window.currentUserId || '';
    var isCurrentUser = message.senderId === currentUserId;
    var cardClass = isCurrentUser ? 'bg-light' : 'bg-white';
    messageElement.className += ' ' + cardClass;
    
    var timestamp = new Date(message.timestamp).toLocaleTimeString();
    var recipientInfo = '';
    if (!message.broadcast) {
        recipientInfo = ' <span class="text-muted">(private)</span>';
    }
    
    messageElement.innerHTML = 
        '<div class="card-body p-2">' +
        '<strong>' + escapeHtml(message.senderLogin) + '</strong>' + recipientInfo +
        ' <span class="text-muted small">' + timestamp + '</span>' +
        '<div>' + escapeHtml(message.content) + '</div>' +
        '</div>';
    
    messagesDiv.appendChild(messageElement);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

/**
 * Escapes HTML special characters to prevent XSS attacks.
 * 
 * @param text The text to escape
 * @returns {string} The escaped text
 */
function escapeHtml(text) {
    if (!text || typeof text !== 'string') return '';
    var div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}
