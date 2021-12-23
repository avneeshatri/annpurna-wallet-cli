import {fetchPromise} from './CustomUtilities'

var create_wallet_uri = '/wallet'
var create_partner_wallet_uri = '/wallet/createPartner'
var read_wallet_uri = '/wallet'
var balance_of_uri = '/wallet/balance'
var add_fund_uri = '/wallet/funds'
var transfer_to_uri = '/wallet/transferTo';
var transfer_uri = '/wallet/transfer';

export function createWallet(callbackFunc){
    var options = { 'method': 'POST' ,'headers': {
        'Content-Type': 'application/json',
      } }
    fetchPromise(create_wallet_uri,options).then(
        response => callbackFunc(response)
    )
}

export function addFunds(fund,callbackFunc){
    var options = { 'method': 'PUT','headers': {
        'Content-Type': 'application/json',
      } }
    var uri = add_fund_uri + '?fund=' + encodeURIComponent(fund)
    fetchPromise(uri,options).then(
        response => callbackFunc(response)
    )
}

export function createPartnerWallet(mspId,callbackFunc){
    var options = { 'method': 'POST','headers': {
        'Content-Type': 'application/json',
      } }
    var uri = create_partner_wallet_uri + '/' + mspId
    fetchPromise(uri,options).then(
        response => callbackFunc(response)
    )
}

export function transferTo(walletId,amount,callbackFunc){
    var options = { 'method': 'PUT','headers': {
        'Content-Type': 'application/json',
      }, 'body' : JSON.stringify({
        recipientWalletId: walletId,
        amount: parseInt(amount)
      })
    }
    var uri = transfer_to_uri 
    fetchPromise(uri,options).then(
        response => callbackFunc(response)
    )
}

export function transfer(senderWalletSecret,senderWalletId,recipientWalletId,amount,callbackFunc){
    var options = { 'method': 'PUT','headers': {
        'Content-Type': 'application/json',
      }, 'body' : JSON.stringify({
        senderSecret: senderWalletSecret,
        senderWalletId: senderWalletId,
        recipientWalletId: recipientWalletId,
        amount: parseInt(amount)
      })
    }
    var uri = transfer_uri 
    fetchPromise(uri,options).then(
        response => callbackFunc(response)
    )
}

export function readWallet(walletSecret,callbackFunc){
    var options = { 'method': 'GET','headers': {
        'Content-Type': 'application/json',
        'x-user-secret': walletSecret
      } }
    var uri = read_wallet_uri
    fetchPromise(uri,options).then(
        response => callbackFunc(response)
    )
}

export function balanceOf(walletSecret,callbackFunc){
    var options = { 'method': 'GET','headers': {
        'Content-Type': 'application/json',
        'x-user-secret': walletSecret
      } }
    var uri = balance_of_uri
    fetchPromise(uri,options).then(
        response => callbackFunc(response)
    )
}