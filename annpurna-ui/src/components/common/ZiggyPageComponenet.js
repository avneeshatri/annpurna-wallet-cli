import { Component } from "react";
import Popup from 'reactjs-popup';
import 'reactjs-popup/dist/index.css';
import { isUndefined, displayCustomPopup } from './CustomUtilities'
import {  getUserWalletSecret, hasUserWalletSecret,setUserWalletSecret,purchase } from './WalletService'



class ZiggyPageComponenet extends Component {
    constructor() {
        super();
        this.state = {
            isOpen: false,
            productPrice: 0
        };
    }

    componentDidMount(){
        var price = Math.floor(Math.random() * 90 + 10)
        console.log("Product Price :"+price)
        this.setState({productPrice: price})
    }

    handleOpen = () => {
        this.setState({ isOpen: true });
    }

    handleClose = () => {
        this.setState({ isOpen: false });
        const walletSecret = document.querySelector("#walletSecret").value;
        if(walletSecret) {
            setUserWalletSecret(walletSecret)
        } else {
            var response = {
                msg:"Wallet secret cannot be blank"
            }

            displayCustomPopup(response,"error")
        }
    }

    transferFromUserToUserWallet(){
        if(hasUserWalletSecret()) {
            const senderWalletSecret = getUserWalletSecret();
            const amount = this.state.productPrice;
            purchase(senderWalletSecret, amount , response =>{
                console.log(response)
            
            } )
        } else {
            var response = {
                msg:"Wallet secret not added in cookies"
            }

            displayCustomPopup(response,"error")
        }
    }

    render() {
        return (

            <div className='container'>
                <hr style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor: '#000000'
                }} />
                <h3>Add User Wallet Private Key</h3>
                <label>User provides its private key for his wallet which will be used for performing transactions.</label>
                <div>
                    <Popup trigger={<button className="btn btn-info btn-sm"> Click to add secret key</button>}
                        position="right center"
                        open={this.state.isOpen}
                        onOpen={this.handleOpen}
                    >
                        <div className="content">
                            <div className="col-lg-1">
                                <input type="text" id="walletSecret" placeholder='Enter Private Key' size="12"></input>
                            </div>
                            <div className="col-sm">
                                <input type="button" className="btn btn-success btn-sm"
                                    value='Add Key to cookies' onClick={this.handleClose} />
                            </div>
                        </div>
                    </Popup>
                </div>
                <hr style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor: '#000000'
                }} />
               <h3>Purchase Item of amount : {this.state.productPrice}</h3>
               <div className="row">
                    <div className="col-sm">
                        <input type="button" className="btn btn-info btn-sm"
                            value='Buy'  onClick={(e) => this.transferFromUserToUserWallet()}  />
                    </div> 
                </div>
            </div>
        );

    }
}

export default ZiggyPageComponenet;