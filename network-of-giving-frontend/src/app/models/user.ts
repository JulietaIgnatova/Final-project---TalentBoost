import { Role } from "./role";

export class User {
    id: number;
    username: string;
    password: string;
    age: number;
    gender: string;
    location: string;
    role: Role;
    token?:string;
}